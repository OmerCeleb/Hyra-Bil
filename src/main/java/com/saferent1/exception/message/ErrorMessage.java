package com.saferent1.exception.message;

public class ErrorMessage {

    public final static String RESOURCE_NOT_FOUND_EXCEPTION = "Resurs med id %s hittades inte";
    public final static String ROLE_NOT_FOUND_EXCEPTION = "Rollen : %s hittades inte";
    public final static String USER_NOT_FOUND_EXCEPTION = "Användare : %s hittades inte";
    public final static String USER_CANT_BE_DELETED_MESSAGE = "Användaren kunde inte tas bort, användaren används av reservation";
    public final static String JWTTOKEN_ERROR_MESSAGE = "JWT-tokenvalideringsfel: %s";
    public final static String EMAIL_ALDREADY_EXIST_MESSAGE = "E-postameddelande: %s finns redan";
    public final static String PRINCIPAL_FOUND_MESSAGE = "Användaren hittades inte";
    public final static String NOT_PERMITTED_METHOD_MESSAGE = "Du har ingen behörighet att ändra denna data";
    public final static String PASSWORD_NOT_MATCHED_MESSAGE = "Dina lösenord matchas inte.";
    public final static String IMAGE_NOT_FOUND_MESSAGE = "Bildfil med id %s hittades inte";
    public final static String IMAGE_USED_MESSAGE = "Bildfilen använde en annan bil";
    public final static String RESERVATION_TIME_INCORRECT_MESSAGE = "Bokningstid för hämtning eller avgångstid är inte korrekt";
    public final static String RESERVATION_STATUS_CANT_CHANGE_MESSAGE = "Bokning kan inte uppdateras för avbokningar eller gjorda bokningar";
    public final static String CAR_NOT_AVAILABLE_MESSAGE = "Bilen är inte tillgänglig under den valda tiden";
    public final static String CAR_USED_BY_RESERVATION_MESSAGE = "Bilen kunde inte raderas, bilen används av reservation";
    public final static String EXCEL_REPORT_ERROR_MESSAGE = "Ett fel uppstod när excel-rapporten skapades";

}
