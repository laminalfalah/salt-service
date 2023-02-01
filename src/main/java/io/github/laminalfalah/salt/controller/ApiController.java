package io.github.laminalfalah.salt.controller;

/*
 * Copyright (C) 2023 the original author laminalfalah All Right Reserved.
 *
 * io.github.laminalfalah.salt.controller
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

import io.github.laminalfalah.salt.payload.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

/**
 * @author laminalfalah on 31/01/23
 */

public interface ApiController {

    private HttpHeaders getHeaders() {
        var header = new HttpHeaders();
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return header;
    }

    default <T> ResponseEntity<Response<T>> toJson(T result) {
        return toJson(result, HttpStatus.OK, null);
    }

    default <T> ResponseEntity<Response<T>> toJson(T result, HttpStatus status, @Nullable String message) {
        return ResponseEntity.status(status)
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response(result, status, message));
    }

    default <T> Response<T> response(T data, HttpStatus status, @Nullable String message) {
        var response = Response.<T>builder()
                .code(status.value())
                .message(message == null ? status.getReasonPhrase() : message)
                .timestamp(System.currentTimeMillis());

        return status.isError() ? response.errors(data).build() : response.data(data).build();
    }

}
