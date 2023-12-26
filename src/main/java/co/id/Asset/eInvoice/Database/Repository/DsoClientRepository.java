package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.DsoClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DsoClientRepository extends JpaRepository<DsoClient, Integer> {
    List<DsoClient> findByClientCode(String clientCode);
}
