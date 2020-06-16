package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

public class V1_0_0__CreateEventTable extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        final JdbcTemplate template = new JdbcTemplate(context.getConnection());
        template.execute("CREATE TABLE events (\n"
                + "idx BIGINT primary key,\n"
                + "title VARCHAR(30),\n"
                + "description VARCHAR(500),\n"
                + "host VARCHAR(255),\n"
                + "address VARCHAR(255),\n"
                + "event_starts TIMESTAMP,\n"
                + "event_ends TIMESTAMP\n"
                + ");"
        );
    }
}
