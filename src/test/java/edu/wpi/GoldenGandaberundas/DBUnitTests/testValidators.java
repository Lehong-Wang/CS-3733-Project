package edu.wpi.GoldenGandaberundas.DBUnitTests;



import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.GoldenGandaberundas.controllers.validators.RequestValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testValidators {

    RequestValidator emptyRVNote = new RequestValidator("");
    RequestValidator emptyRVGift = new RequestValidator("","");
    RequestValidator emptyRVMedicine = new RequestValidator("", "", "");

    RequestValidator dropTableRVNote = new RequestValidator("DROP TABLE");
    RequestValidator dropTableRVGift = new RequestValidator("DROP TABLE","DROP TABLE");
    RequestValidator dropTableRVMedicine = new RequestValidator("DROP TABLE", "DROP TABLE", "DROP TABLE");

    RequestValidator rvNoteNormal = new RequestValidator("this is a request note!?@#$%^&*()_+=-0987654321`~/?.,<>';:|][}{");
    RequestValidator rvGiftNormal = new RequestValidator("this is a Gift request note!?@#$%^&*()_+=-0987654321`~/?.,<>';:|][}{","DROP TABLE");
    RequestValidator rvMedicineNormal = new RequestValidator("this is a Medicine request note!?@#$%^&*()_+=-0987654321`~/?.,<>';:|][}{", "DROP TABLE", "DROP TABLE");

}
