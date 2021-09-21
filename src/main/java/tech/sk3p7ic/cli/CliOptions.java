package tech.sk3p7ic.cli;

import picocli.CommandLine;

public class CliOptions {
  @CommandLine.Option(names = {"-c", "--createProject"}, description = "Create a project file for the input files.")
  boolean createProject;

  @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help message.")
  private boolean helpRequested = false;
}
