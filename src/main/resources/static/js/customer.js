// Vanilla Javascript

const columns = {
    customer: [
        {
            data: 'createdAt',
            name: 'createdAt',
            title: 'No.',
            className: 'text-left',
            width: '5%',
            searchable: false,
        },
        {
            data: 'name',
            name: 'name',
            title: 'Name',
            width: '15%',
            className: 'text-left'
        },
        {
            data: 'city',
            name: 'city',
            title: 'City',
            width: '15%',
            className: 'text-left'
        },
        {
            data: 'state',
            name: 'state',
            title: 'State',
            width: '15%',
            className: 'text-left'
        },
        {
            data: 'dateRegistration',
            name: 'dateRegistration',
            title: 'Date Registration',
            width: '15%',
            className: 'text-center',
            orderable: true,
            searchable: false,
            render: (data) => typeof (data) !== 'number' ? data : unixTimestampToDate(data)
        },
        {
            data: 'status',
            name: 'status',
            title: 'Status',
            className: 'text-center',
            width: '10%',
            orderable: true,
            searchable: false,
            render: (data, type) => renderStatus(data, type)
        },
        {
            data: null,
            title: 'Action',
            className: 'text-center',
            width: '15%',
            searchable: false,
            orderable: false,
            defaultContent: '',
            render: (data) => renderAction(data.id)
        },
    ]
}

const getColumn = (key) => columns[key];

const createColumnTable = (attr, columns) => {
    const trows = document.createElement("tr");

    for (const column of Object.values(columns)) {
        const header = document.createElement("th");
        header.scope = "col"
        header.className = column.className;
        header.style.width = column.width;
        header.setAttribute("data-target", column.name);
        header.innerText = column.title;
        trows.appendChild(header);
    }

    attr.appendChild(trows);
}

const createHeaderTable = (columns) => {
    const thead = document.querySelector("thead");
    while (thead.firstChild) {
        thead.removeChild(thead.firstChild);
    }
    createColumnTable(thead, columns);
}

const createFooterTable = (columns) => {
    const tfoot = document.querySelector("tfoot");
    while (tfoot.firstChild) {
        tfoot.removeChild(tfoot.firstChild);
    }
    createColumnTable(tfoot, columns);
}

const unixTimestampToDate = (timestamp) => {
    const dateTime = new Date(timestamp);

    const year = dateTime.getFullYear();
    const month = ('0' + (dateTime.getMonth() + 1)).slice(-2);
    const day = ('0' + dateTime.getDate()).slice(-2);
    const hour = ('0' + dateTime.getHours()).slice(-2);
    const minute = ('0' + dateTime.getMinutes()).slice(-2);
    const second = ('0' + dateTime.getSeconds()).slice(-2);

    return day + "-" + month + "-" + year + ", " + hour + ":" + minute + ":" + second;
}

const renderStatus = (data, type) => {
    let color = "text-muted", text = data;
    if (type === 'display') {
        if (data === "ACTIVE") {
            color = "text-primary";
            text = "Active";
        } else {
            color = "text-danger";
            text = "Not Active";
        }
    }
    return '<span class="' + color + '">' + text + '</span>';
}

const renderAction = (data) => {
    const btnEdit = renderButtonAction("Edit", data, "btn-primary", "bi-pencil-square", "editModal");
    const btnShow = renderButtonAction("Show", data, "btn-dark", "bi-eye", "showModal");
    const btnDelete = renderButtonAction("Delete", data, "btn-danger", "bi-trash", "deleteModal");

    return btnEdit + ' ' + btnShow + ' ' + btnDelete;
}

const renderButtonAction = (title, data, classBtn, icon, modal) => {
    const m = modal !== undefined ? 'data-bs-toggle="modal" data-bs-target="#' + modal + '" ' : '';
    return '<a ' +
        'id="' + title.toLowerCase() +
        '" href="javascript:void(0)" class="btn btn-sm ' + classBtn +
        '" title="' + title +
        '" data-id="' + data +
        '"' + m +
        '><i class="bi ' + icon + '"></i></a>';
}

const beforeSendAjax = () => {
    const processing = document.querySelector("#table_customer_processing");
    const text = document.createElement("span");
    processing.className = "text-center";
    text.innerText = "Loading Data...";
    while (processing.firstChild) {
        processing.removeChild(processing.firstChild);
    }
    processing.appendChild(text);
}

const failAjax = () => {
    const error = document.querySelector(".table_customer-error");
    if (error) {
        error.innerHTML = "";
    }
    document.querySelector("#table_customer_processing").style.display = "none";
    const table = document.querySelector("#table_customer");
    const tbody = document.createElement("tbody");
    const tr = document.createElement("tr");
    const td = document.createElement("td");

    tbody.className = "table-customer-error";
    td.colSpan = columns.customer.length;
    td.className = "text-center";
    td.innerText = "Data not found";
    tr.append(td);
    tbody.append(tr);
    table.append(tbody);
}

document.addEventListener('DOMContentLoaded', () => {
    showLoading();

    onReady(() => hideLoading());

    const customer = getColumn("customer");
    createHeaderTable(customer);
    createFooterTable(customer);

    const api = 'http://localhost:8080/api/v1';

    const tableCustomer = new DataTable('#table_customer', {
        ajax: (_, callback, settings) => {
            $.ajax({
                url: api + '/customers',
                type: 'GET',
                dataType: 'JSON',
                data: settings.oAjaxData,
                timeout: 5000,
                beforeSend: () => beforeSendAjax()
            }).done((json) => {
                callback({
                    recordsTotal: json.data.recordsTotal,
                    recordsFiltered: json.data.recordsFiltered,
                    data: json.data.data
                });
            }).fail((jqXHR) => failAjax());
        },
        serverSide: true,
        processing: true,
        scrollX: false,
        scrollY: false,
        searchDelay: 3000,
        paginationType: "full_numbers",
        pageLength: 10,
        order: [
            [0, 'desc']
        ],
        columns: columns.customer,
        autoWidth: false
    }).on("draw.dt", () => {
        const info = tableCustomer.page.info();
        tableCustomer.column(0, {
            search: "applied",
            order: "applied",
            page: "applied",
        }).nodes().each((cell, i) => {
            cell.innerHTML = i + 1 + info.start + ".";
        });
    });

    document.querySelector("#refresh").addEventListener('click', (e) => {
        e.preventDefault();
        tableCustomer.ajax.reload();
    });

    function convertFormToJson(form) {
        return $(form).serializeArray().reduce(function (json, { name, value }) {
            json[name] = value;
            return json;
        }, {});
    }

    $("#createForm").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: api + '/customers',
            type: 'POST',
            dataType: 'JSON',
            data: JSON.stringify(convertFormToJson(this)),
            timeout: 1500,
            contentType: 'application/json'
        }).done((json) => {
            $(this)[0].reset();
            $(".btn-close").click();
            tableCustomer.ajax.reload();
        }).fail((error) => {
            console.error(error.statusText);
        });
    });

    $("#table_customer").on('click', '#edit', function (e) {
        e.preventDefault();
        const id = $(this).attr('data-id');

        $.ajax({
            url: api + '/customers/' + id,
            type: 'GET',
            dataType: 'JSON',
            timeout: 1500,
            contentType: 'application/json'
        }).done(function (json) {
            $("#editModal").on('shown.bs.modal', function (e) {
                $(this).find("#editModalLabel").text("Edit " + json.data.name);
                $(this).find("#name").val(json.data.name);
                $(this).find("#city").val(json.data.city);
                $(this).find("#state").val(json.data.state);
                $(this).find("#dateRegistration").val(json.data.dateRegistration);
                $(this).find("#address").val(json.data.address);
                $(this).find("#status").val(json.data.status);
            });

            $("#editForm").submit(function (e) {
                e.preventDefault();
                $.ajax({
                    url: api + '/customers/' + json.data.id,
                    type: 'PUT',
                    dataType: 'JSON',
                    data: JSON.stringify(convertFormToJson(this)),
                    timeout: 1500,
                    contentType: 'application/json'
                }).done((json) => {
                    $(this)[0].reset();
                    $(".btn-close").click();
                    tableCustomer.ajax.reload();
                }).fail((error) => {
                    console.error(error.statusText);
                });
            });
        }).fail((error) => {
            console.error(error.statusText);
        });

    }).on('click', '#show', function (e) {
        e.preventDefault();
        const id = $(this).attr('data-id');
        $.ajax({
            url: api + '/customers/' + id,
            type: 'GET',
            dataType: 'JSON',
            timeout: 1500,
            contentType: 'application/json'
        }).done(function (json) {
            $("#showModal").on('shown.bs.modal', function (e) {
                $(this).find("#showModalLabel").text("Show " + json.data.name);
                $(this).find("#name").val(json.data.name);
                $(this).find("#city").val(json.data.city);
                $(this).find("#state").val(json.data.state);
                $(this).find("#dateRegistration").val(json.data.dateRegistration);
                $(this).find("#address").val(json.data.address);
                $(this).find("#status").val(json.data.status);
            });
        }).fail((error) => {
            console.error(error.statusText);
        });
    }).on('click', '#delete', function (e) {
        e.preventDefault();
        const id = $(this).attr('data-id');

        $("#confirmDelete").on('click', function (e) {
            $.ajax({
                url: api + '/customers/' + id,
                type: 'DELETE',
                dataType: 'JSON',
                timeout: 1500,
                contentType: 'application/json'
            }).done(function (json) {
                $(".btn-close").click();
                tableCustomer.ajax.reload();
            }).fail((error) => {
                console.error(error.statusText);
            });
        });
    })

});
