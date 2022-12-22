package ru.programstore.prostore.test.common.command;

import lombok.*;
import ru.programstore.prostore.core.Command;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateShipmentCommand implements Command {
    private String destination;
    private String location;
}
