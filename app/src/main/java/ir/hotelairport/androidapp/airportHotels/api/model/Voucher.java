package ir.hotelairport.androidapp.airportHotels.api.model;

public class Voucher {
    int book_id;
    VoucherData data;

    public Voucher() {
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public VoucherData getData() {
        return data;
    }

    public void setData(VoucherData data) {
        this.data = data;
    }
}
