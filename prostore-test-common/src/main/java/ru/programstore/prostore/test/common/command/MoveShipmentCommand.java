package ru.programstore.prostore.test.common.command;

import lombok.*;
import ru.programstore.prostore.core.Command;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MoveShipmentCommand implements Command {
    private String aggregateId;
    private String location;
}
