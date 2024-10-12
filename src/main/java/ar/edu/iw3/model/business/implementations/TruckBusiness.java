package ar.edu.iw3.model.business.implementations;

import ar.edu.iw3.model.Product;
import ar.edu.iw3.model.Truck;
import ar.edu.iw3.model.business.exceptions.BusinessException;
import ar.edu.iw3.model.business.exceptions.FoundException;
import ar.edu.iw3.model.business.exceptions.NotFoundException;
import ar.edu.iw3.model.business.interfaces.ITruckBusiness;
import ar.edu.iw3.model.persistence.TruckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TruckBusiness implements ITruckBusiness {

    @Autowired
    private TruckRepository truckDAO;

    @Override
    public List<Truck> list() throws BusinessException {
        try {
            return truckDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Truck load(long id) throws NotFoundException, BusinessException {
        Optional<Truck> truckFound;

        try {
            truckFound = truckDAO.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (truckFound.isEmpty())
            throw NotFoundException.builder().message("No se encuentra el Camion id= " + id).build();
        return truckFound.get();
    }

    @Override
    public Truck load(String licensePlate) throws NotFoundException, BusinessException {
        Optional<Truck> truckFound;

        try {
            truckFound = truckDAO.findByLicensePlate(licensePlate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (truckFound.isEmpty())
            throw NotFoundException.builder().message("No se Encuentra el Camion con Patente " + licensePlate).build();

        return truckFound.get();
    }

    @Override
    public Truck add(Truck truck) throws FoundException, BusinessException {
        try {
            load(truck.getId());
            throw FoundException.builder().message("Ya existe el Camion id= " + truck.getId()).build();
        } catch (NotFoundException e) {
            // log.trace(e.getMessage(), e);
        }

        try {
            load(truck.getLicensePlate());
            throw FoundException.builder().message("Ya existe el Camion con Patente " + truck.getLicensePlate()).build();
        } catch (NotFoundException e) {
            // log.trace(e.getMessage(), e);
        }

        try {
            return truckDAO.save(truck);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //throw BusinessException.builder().ex(e).build();
            throw BusinessException.builder().message("Error al Crear Nuevo Camion").build();
        }
    }

    @Override
    public Truck update(Truck truck) throws NotFoundException, BusinessException, FoundException {
        load(truck.getId());

        Optional<Truck> truckFound;
        try {
            truckFound = truckDAO.findByLicensePlateAndIdNot(truck.getLicensePlate(), truck.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if (truckFound.isPresent()) {
            throw FoundException.builder().message("Ya Existe un Camion con Patente =" + truck.getLicensePlate()).build();
        }

        try {
            return truckDAO.save(truck);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //throw BusinessException.builder().ex(e).build();
            throw BusinessException.builder().message("Error al Actualizar Camion").build();
        }
    }

    @Override
    public void delete(Truck truck) throws NotFoundException, BusinessException {
        delete(truck.getId());
    }

    @Override
    public void delete(long id) throws NotFoundException, BusinessException {
        load(id);

        try {
            truckDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }
}
