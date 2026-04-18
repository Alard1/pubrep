package app;

import versatilesetup.Executor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BuildNewMain {

    public static void main(String[] args) throws Exception {

        Path itemPath = Paths.get(args[0]);
        Path charPath = Paths.get(args[1]);

        Executor.execute(itemPath, charPath);
    }
}
