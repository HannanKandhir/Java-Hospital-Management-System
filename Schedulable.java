package hospital.interfaces;

import java.time.LocalDateTime;

public interface Schedulable {
    boolean isAvailable(LocalDateTime dateTime);
}