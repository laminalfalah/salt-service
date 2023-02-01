package io.github.laminalfalah.salt.service;

/*
 * Copyright (C) 2023 the original author laminalfalah All Right Reserved.
 *
 * io.github.laminalfalah.salt.service
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

import io.github.laminalfalah.salt.payload.request.CustomerRequest;
import io.github.laminalfalah.salt.payload.response.CustomerResponse;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laminalfalah on 01/02/23
 */

public interface CustomerService {

    @Transactional(readOnly = true)
    DataTablesOutput<CustomerResponse> index(DataTablesInput parameter);

    @Transactional(rollbackFor = RuntimeException.class)
    CustomerResponse store(CustomerRequest request);

    @Transactional(readOnly = true)
    CustomerResponse show(Long id);

    @Transactional(rollbackFor = RuntimeException.class)
    CustomerResponse update(Long id, CustomerRequest request);

    @Transactional(rollbackFor = RuntimeException.class)
    void delete(Long id);

}
