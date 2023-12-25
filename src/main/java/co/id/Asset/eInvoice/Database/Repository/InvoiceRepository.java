package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query(value = """
            SELECT * 
            FROM Invoice 
            WHERE invoice_number LIKE %:search% 
            OR spk_number LIKE %:search% 
            ORDER BY created_date desc 
            OFFSET :page ROWS 
            FETCH NEXT :limit ROWS ONLY """, nativeQuery = true)
    List<Invoice> getListInvoicePagination(@Param("search") String search,
                                           @Param("page") Integer page,
                                           @Param("limit") Integer limit);
    @Query(value = """
            SELECT COUNT(*) 
            FROM Invoice 
            WHERE invoice_number LIKE %:search% 
            OR spk_number LIKE %:search% """, nativeQuery = true)
    Integer countPaging(@Param("search") String search);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Boolean existsByInvoiceNumber(String invoiceNumber);
}
