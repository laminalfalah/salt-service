package io.github.laminalfalah.salt.model;

/*
 * Copyright (C) 2023 the original author laminalfalah All Right Reserved.
 *
 * io.github.laminalfalah.salt.model
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

import io.github.laminalfalah.salt.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * @author laminalfalah on 30/01/23
 */

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @Column(name = "state", length = 50, nullable = false)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_registration", nullable = false)
    private Date dateRegistration;

    @Enumerated
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Status status;

}
