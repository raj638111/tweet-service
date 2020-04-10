package com.occ.rankingcli;

import com.occ.rankingcli.commands.LS;
import com.occ.rankingcli.commands.Rank;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

/*
    Starting point for the application
 */

@Slf4j
// Register all the commands supported
@CommandLine.Command(subcommands = {LS.class, Rank.class})
public class RankCli implements Runnable{

    public static void main(String[] args) {

        RankCli app = new RankCli();
        CommandLine cmd = new CommandLine(app);

        // Call the appropriate command class (Example: commands.LS)
        // based on the argument passed by the user
        cmd.execute(args);

        // If not argument is passed, Display the help message
        if(args.length == 0){
            cmd.usage(System.out);
        }

    }

    public void run() {
        // This method does nothing, but needed to avoid compilation error
    }

}