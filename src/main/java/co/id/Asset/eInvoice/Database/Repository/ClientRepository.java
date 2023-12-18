package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {
    Boolean existsByClientCode(String clientCode);

    @Query(value = """
            SELECT * 
            FROM Client 
            WHERE client_code LIKE %:param% OR 
            name LIKE %:param%
            ORDER BY client_code asc
            OFFSET :page ROWS
            FETCH NEXT :limit ROWS ONLY
            """,nativeQuery = true)
    List<Client> getListClientPagination(@Param("param") String param,
                                         @Param("page") Integer page,
                                         @Param("limit") Integer limit);
    @Query(value = """
            SELECT COUNT(*) 
            FROM Client 
            WHERE client_code LIKE %:param% OR 
            name LIKE %:param%
            """,nativeQuery = true)
    Integer countData(@Param("param") String param);
}
