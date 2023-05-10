package pro.sky.sockswarehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.sockswarehouse.model.SocksDto;
import pro.sky.sockswarehouse.service.SocksService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@RequestMapping("/socks")
@RequiredArgsConstructor
public class SocksController {

    private final SocksService socksService;

    @Operation(summary = "addSocks", description = "Добавление носков на склад",
            tags = {"Склад"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление носков",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SocksDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
            })
    @PostMapping("/income")
    public ResponseEntity<?> addSocks(@Valid @RequestBody SocksDto socksDto) {
        log.info("Add socks in warehouse " + socksDto);
        socksService.addSocks(socksDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "removeSocks", description = "Удаление носков со склада",
            tags = {"Склад"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Удаление носков",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SocksDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
            })
    @PostMapping("/outcome")
    public ResponseEntity<?> removeSocks(@Valid @RequestBody SocksDto socksDto) {
        log.info("Remove socks from warehouse " + socksDto);
        socksService.removeSocks(socksDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "getAmountSocks", description = "Получение кол-ва носков на складе",
            tags = {"Склад"},
            parameters = {
                    @Parameter(name = "color", description = "Цвет носков", example = "black"),
                    @Parameter(name = "operation", description = "Оператор сравнения кол-ва хлопка в носках",
                            example = "lessThen"),
                    @Parameter(name = "cottonPart", description = "Кол-во хлопка для сравнения", example = "40")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
            })
    @GetMapping
    public ResponseEntity<Integer> getAmountSocks(
            @RequestParam @NotEmpty String color,
            @RequestParam @NotEmpty String operation,
            @RequestParam @Range(min = 0, max = 100) Integer cottonPart) {
        log.info("Get request to find amount of " + color + " socks, where cottonPart " + operation + " " + cottonPart);
        return ResponseEntity.ok(socksService.getAmountOfSocks(color, operation, cottonPart));
    }
}
