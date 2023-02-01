package io.github.laminalfalah.salt.service.impl;

/*
 * Copyright (C) 2023 the original author laminalfalah All Right Reserved.
 *
 * io.github.laminalfalah.salt.service.impl
 *
 * This is part of the salt-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.laminalfalah.salt.exception.DataNotFoundException;
import io.github.laminalfalah.salt.mapper.CustomerMapper;
import io.github.laminalfalah.salt.payload.request.CustomerRequest;
import io.github.laminalfalah.salt.payload.response.CustomerResponse;
import io.github.laminalfalah.salt.repository.CustomerRepository;
import io.github.laminalfalah.salt.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author laminalfalah on 01/02/23
 */

@Service
@Validated
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    @Override
    public DataTablesOutput<CustomerResponse> index(DataTablesInput parameter) {
        var data = repository.findAll(
                parameter,
                (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"))
        );

        var datatable = new DataTablesOutput<CustomerResponse>();
        datatable.setDraw(data.getDraw());
        datatable.setRecordsTotal(data.getRecordsTotal());
        datatable.setRecordsFiltered(data.getRecordsFiltered());
        datatable.setData(data.getData().stream().map(mapper::toDto).collect(Collectors.toList()));
        datatable.setSearchPanes(data.getSearchPanes());
        datatable.setError(data.getError());
        return datatable;
    }

    @Override
    public CustomerResponse store(CustomerRequest request) {
        return mapper.toDto(repository.save(mapper.toEntity(request)));
    }

    @Override
    public CustomerResponse show(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest request) {
        return repository.findById(id)
                .map(v -> mapper.toUpdateEntity(v, request))
                .map(mapper::toDto)
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id)
                .map(v -> {
                    v.setDeletedBy("WEB");
                    v.setDeletedAt(new Date());
                    repository.save(v);
                    return Void.class;
                })
                .orElseThrow(DataNotFoundException::new)
                .isAssignableFrom(Void.class);
    }

}
