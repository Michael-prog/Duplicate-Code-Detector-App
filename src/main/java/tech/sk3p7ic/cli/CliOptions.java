package tech.sk3p7ic.cli;

import picocli.CommandLine;

import java.io.File;

public class CliOptions {
  @CommandLine.Parameters(paramLabel = "File 1", description = "The first file to scan.")
  public File file1;

  @CommandLine.Option(names = {"-c", "--createProject"}, description = "Create a project file for the input files.")
  public boolean createProject;

  @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help message.")
  public boolean helpRequested = false;
}
