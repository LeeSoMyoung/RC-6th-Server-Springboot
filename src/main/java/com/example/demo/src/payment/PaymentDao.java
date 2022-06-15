package com.example.demo.src.payment;

import com.example.demo.src.payment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PaymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private void    setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPaymentRes>   getPaymentInfo(long userId){
        String getUserPaymentInfo = "SELECT\n" +
                "    R.price AS 'Payment price',\n" +
                "    R.description   AS 'Receipt description',\n" +
                "    R.status        AS 'Purchased status',\n" +
                "    M.price     AS 'Membership price',\n" +
                "    M.membershipName         AS 'Membership description',\n" +
                "    R.receiptId AS 'Receipt PK'\n" +
                "FROM\n" +
                "    (Receipts R inner join Users U on R.userId = U.userId)\n" +
                "    inner join ChannelMembership M on M.userId = U.userId\n" +
                "WHERE\n" +
                "    U.userId = ?\n" +
                "    AND U.status = 'ACTIVE';";
        long    getUserPaymentInfoParams = userId;

        return  this.jdbcTemplate.query(
                getUserPaymentInfo,
                (rs, rowNum) ->
                    new GetPaymentRes(
                            rs.getInt("Payment price"),
                            rs.getString("Receipt description"),
                            rs.getString("Purchased status"),
                            rs.getInt("Membership price"),
                            rs.getString("Membership description"),
                            rs.getLong("Receipt PK")
                    )
                ,
                getUserPaymentInfoParams
        );
    }

    public List<CardInfo>   getCardInfoLists(long   userId)  {
        String  getCardInfoListQuery = "SELECT\n" +
                "    paymentId   AS 'Payment PK',\n" +
                "    card        AS 'Card info',\n" +
                "    concat('***', right(cardNumber,3))  AS 'card number'\n" +
                "FROM\n" +
                "    (Payment P inner join Users U on U.userId = P.userId)\n" +
                "WHERE\n" +
                "    U.userId = ?\n" +
                "    AND U.status = 'ACTIVE'\n" +
                "    AND P.status = 'Y';";
        long  getCardInfoListQueryParams = userId;

        return  this.jdbcTemplate.query(
                getCardInfoListQuery,
                (rs, rowNum) -> new CardInfo(
                        rs.getLong("Payment PK"),
                        rs.getString("Card Info"),
                        rs.getString("card number")
                ),
                getCardInfoListQueryParams
        );
    }

    public long         createPaymentInfo(PostPaymentReq postPaymentReq){
        String      createPaymentQuery = "INSERT INTO Payment(userId, card, cardNumber, CVC, password)\n" +
                "VALUE(?, ?, ?,?,?);";
        Object[]    createPaymentQueryParams = new  Object[]{postPaymentReq.getUserId(), postPaymentReq.getCard(), postPaymentReq.getCardNum(), postPaymentReq.getCVC(), postPaymentReq.getPassword()};
        this.jdbcTemplate.update(createPaymentQuery,createPaymentQueryParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,long.class);
    }

    public int          checkCardInfos(String card, String cardNum){
        String      checkInfoQuery = "SELECT EXISTS(SELECT card, cardNum FROM Payment WHERE card = ? and cardNum = ?;";
        Object[]    checkInfoQueryParams = new Object[]{card, cardNum};

        return  this.jdbcTemplate.queryForObject(checkInfoQuery,int.class,checkInfoQueryParams);
    }
}