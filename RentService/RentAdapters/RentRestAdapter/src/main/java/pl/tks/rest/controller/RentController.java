package pl.tks.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tks.model.Rent;
import pl.tks.ports.ui.IRentPort;
import pl.tks.rest.dto.RentDTO;
import pl.tks.rest.mapper.RentMapper;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/rents")
public class RentController {

    private final IRentPort rentPort;
    private final RentMapper rentMapper;

    public RentController(IRentPort rentPort, RentMapper rentMapper) {
        this.rentPort = rentPort;
        this.rentMapper = rentMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentDTO rentItem(@RequestBody @Valid RentDTO rentDTO) {
        if (rentDTO.getBeginTime() == null) {
            rentDTO.setBeginTime(LocalDateTime.now());
        }
        Rent rent = rentPort.add(rentMapper.toDomain(rentDTO));
        return rentMapper.convertToDTO(rent);
    }

    @PutMapping("return/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnItem(@PathVariable String id) {
        rentPort.returnRent(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentDTO> getRent(@PathVariable String id) {
        Rent rent = rentPort.getById(id);
        return ResponseEntity.ok(rentMapper.convertToDTO(rent));
    }

    @GetMapping("/active")
    public ResponseEntity<List<RentDTO>> getActiveRents() {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.getActiveRents()));
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<RentDTO>> getInactiveRents() {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.getInactiveRents()));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<RentDTO>> getRentsByItem(@PathVariable String itemId) {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.getByItemId(itemId)));
    }

    @GetMapping("/active/item/{itemId}")
    public ResponseEntity<List<RentDTO>> getActiveRentsByItem(@PathVariable String itemId) {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.findActiveRentsByItemId(itemId)));
    }

    @GetMapping("/inactive/item/{itemId}")
    public ResponseEntity<List<RentDTO>> getInactiveRentsByItem(@PathVariable String itemId) {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.findInactiveRentsByItemId(itemId)));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RentDTO>> getRentsByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.getByClientId(clientId)));
    }

    @GetMapping("/active/client/{clientId}")
    public ResponseEntity<List<RentDTO>> getActiveRentsByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.findActiveRentsByClientId(clientId)));
    }

    @GetMapping("/inactive/client/{clientId}")
    public ResponseEntity<List<RentDTO>> getInactiveRentsByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(rentMapper.toDTO(rentPort.findInactiveRentsByClientId(clientId)));
    }

    @GetMapping("/isRented/{itemId}")
    public ResponseEntity<Boolean> isItemRented(@PathVariable String itemId) {
        return ResponseEntity.ok(!rentPort.findActiveRentsByItemId(itemId).isEmpty());
    }
}
