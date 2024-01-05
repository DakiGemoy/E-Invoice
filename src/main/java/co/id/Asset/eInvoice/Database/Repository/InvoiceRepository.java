package co.id.Asset.eInvoice.Database.Repository;

import co.id.Asset.eInvoice.Database.Entity.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    @Query(value = """
            SELECT * 
            FROM Invoice 
            WHERE invoice_number LIKE %:search% 
            OR spk_number LIKE %:search%
            ORDER BY is_reminder ASC, 
            DATEDIFF(DAY, GETDATE(), due_date) ASC 
            """, nativeQuery = true)
    List<Invoice> getPagination(@Param("search") String search,
                                       Pageable pageable);

    @Query(value = """
            SELECT * 
            FROM Invoice 
            WHERE  
            CAST(created_date AS DATE) >= :rangeFrom AND 
            CAST(created_date AS DATE) <= :rangeTo AND 
            ( spk_number LIKE %:search% OR  
            invoice_number LIKE %:search% ) 
            ORDER BY is_reminder ASC,
            DATEDIFF(DAY, GETDATE(), due_date) ASC 
            """, nativeQuery = true)
    List<Invoice> getPagination(@Param("search") String search,
                                       @Param("rangeFrom") LocalDate rangeFrom,
                                       @Param("rangeTo") LocalDate rangeTo,
                                       Pageable pageable);

    @Query(value = """
            SELECT * 
            FROM Invoice 
            WHERE CAST(created_date AS DATE) >= :rangeFrom AND 
            CAST(created_date AS DATE) <= :rangeTo AND 
            is_draft = 0 AND 
            ( sendToExcel = 0 OR sendToExcel IS NULL )
            """, nativeQuery = true)
    List<Invoice> getDataToExcel(@Param("rangeFrom") LocalDate dateFrom,
                                 @Param("rangeTo") LocalDate dateTo);
}
