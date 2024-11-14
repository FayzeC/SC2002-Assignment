package appointmentoutcomerecordsystem;

import java.io.IOException;

public interface updateOutcomeRecord {
    void updateAppointmentOutcomeRecord(String appointmentID, String header, String newValue) throws IOException;
}
