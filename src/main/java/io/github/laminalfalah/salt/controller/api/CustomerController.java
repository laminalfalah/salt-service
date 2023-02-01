package io.github.laminalfalah.salt.controller.api;

/*
 * Copyright (C) 2023 the original author laminalfalah All Right Reserved.
 *
 * io.github.laminalfalah.salt.controller.api
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

import io.github.laminalfalah.salt.controller.ApiController;
import io.github.laminalfalah.salt.payload.request.CustomerRequest;
import io.github.laminalfalah.salt.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author laminalfalah on 30/01/23
 */

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController implements ApiController {

    private final CustomerService service;

    @GetMapping
    public ResponseEntity<?> index(@Valid DataTablesInput dataTable) {
        return toJson(service.index(dataTable));
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CustomerRequest request) {
        return toJson(service.store(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        return toJson(service.show(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody CustomerRequest request) {
        return toJson(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") Long id) {
        service.delete(id);
        return toJson(null);
    }

}
