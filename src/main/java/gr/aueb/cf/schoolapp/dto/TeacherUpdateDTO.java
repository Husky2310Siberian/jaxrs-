package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherUpdateDTO {

    @NotNull (message = "Το id δεν μπορεί να μην υπάρχει.")
    private Long id;

    @NotNull(message = "Το όνομα δεν μπορεί να μην υπάρχει.")
    @Size(min = 2, max = 255, message = "Name must contains 2 to 255 characters")
    private String firstname;

    @NotNull (message = "Το επώνυμο δεν μπορεί να μην υπάρχει.")
    @Size(min = 2, max = 255, message = "Surname must contains 2 to 255 characters")
    private String lastname;

    @NotNull(message = "Το ΑΦΜ δεν μπορεί να μην υπάρχει.")
    @Size(min = 9, message = "VAT must contains at least 9 digits")
    private String vat;
}
