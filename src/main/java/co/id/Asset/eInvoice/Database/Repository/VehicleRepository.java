package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    @Query(value = "SELECT * " +
            "FROM Vehicle as ve " +
            "WHERE CONCAT(unique_number,' ',vehicle_type) LIKE %:search% AND " +
            "ve.id not in (" +
            "SELECT vehicle_id FROM DescriptionList " +
            "WHERE rent_from <= GETDATE() AND rent_to >= GETDATE())",nativeQuery = true)
    List<Vehicle> getDataCar(@Param("search") String search);
}
