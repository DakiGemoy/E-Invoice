package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DescriptionRepository extends JpaRepository<Description, Long> {
    List<Description> findByInvoiceNumber(String invoiceNumber);
}
