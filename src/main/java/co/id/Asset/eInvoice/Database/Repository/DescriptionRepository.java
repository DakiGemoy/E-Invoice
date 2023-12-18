package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.Description;
import co.id.Asset.eInvoice.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DescriptionRepository extends JpaRepository<Description, Integer> {
    Optional<Description> findByInvoiceNumberAndVehicleId(String invoiceNumber, Integer vehicleId);
    @Query("""
            SELECT new co.id.Asset.eInvoice.Model.Item (invoiceNumber, vehicleId, rentFrom, rentTo)
            FROM Description 
            WHERE invoiceNumber = :invoiceNumber
            """)
    List<Item> getDescByInvoiceNumber(@Param("invoice") String invoiceNumber);
}
