package io.github.laminalfalah.salt.payload.request;

/*
 * Copyright (C) 2023 the original author laminalfalah All Right Reserved.
 *
 * io.github.laminalfalah.salt.payload.request
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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.laminalfalah.salt.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author laminalfalah on 30/01/23
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest implements Serializable {

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 500)
    private String address;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50)
    private String city;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50)
    private String state;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateRegistration;

    @NotNull
    @Enumerated
    private Status status;

}
