package org.devnotfound.testcontainerscrontask;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CronTask {

    private static final Logger logger = LoggerFactory.getLogger(CronTask.class);

    public int deletedFiles;
    private final CLI cli;

    public CronTask() {
        deletedFiles = 0;
        this.cli = new CLI();
    }

    int start() throws Exception {
        List<String> ids;
        try {
            ids = cli.getContainersId();
        } catch (IOException | InterruptedException e) {
            logger.warn("Exception occurred: " + e.getClass());
            logger.warn("With message: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            logger.debug("Current task will now end");
            return 1;
        }

        for (String id :
                ids) {
            if (id != null)
                if(Json.isFromTestContainers(cli.inspect(id))) {
                cli.kill(id);
                deletedFiles ++;
            }
        }

        return 0;
    }
}
