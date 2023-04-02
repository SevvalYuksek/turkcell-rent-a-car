package kodlama.io.rentacar.business.concretes;


import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenancesResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final CarService carService;
    private final ModelMapper mapper;

    @Override
    public List<GetAllMaintenancesResponse> getAll() {
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();

        List<GetAllMaintenancesResponse> response = maintenanceList
                .stream()
                .map(maintenance -> mapper.map(maintenance, GetAllMaintenancesResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetMaintenanceResponse getById(int id) {
        checkIfMaintenanceExistsById(id);
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow();

        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        checkIfCarState(request.getCarId());

        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(0);
        Maintenance createdMaintenance = maintenanceRepository.save(maintenance);

        CreateMaintenanceResponse response = mapper.map(createdMaintenance, CreateMaintenanceResponse.class);
        return response;
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) {
        return null;
    }

    @Override
    public void delete(int id) {
        checkIfMaintenanceExistsById(id);
        changeExitingCarState(id);
        maintenanceRepository.deleteById(id);
    }

    private void checkIfMaintenanceExistsById(int id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new RuntimeException("Maintenance does not exists with id: " + id);
        }
    }

    private void changeExitingCarState(int id) {
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow();
        maintenance.getCar().setState(State.AVAILABLE);
    }

    private void checkIfCarState(int carId) {
        Car car = carService.findCarById(carId);
        if(!(car.getState().toString() == "AVAILABLE")) {
            throw new RuntimeException("Car is not available");
        }
        car.setState(State.MAINTENANCE);
    }
}
