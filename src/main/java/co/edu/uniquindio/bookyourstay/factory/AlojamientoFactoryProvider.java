package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;

public class AlojamientoFactoryProvider {
    public static AlojamientoFactory getFactory(TipoAlojamiento tipo) {
        return switch (tipo) {
            case CASA -> new CasaFactory();
            case HOTEL -> new HotelFactory();
            case APARTAMENTO -> new ApartamentoFactory();
        };
    }
}