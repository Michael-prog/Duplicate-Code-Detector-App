package tech.sk3p7ic.cli;

import picocli.CommandLine;

import java.io.File;

public class CliOptions {
  @CommandLine.Option(names = {"-g", "--gui"}, description = "Start the application in a graphical mode.")
  public boolean useGUI = false;

  @CommandLine.Option(names = {"-c", "--createProject"}, description = "Create a project file for the input files.")
  public boolean createProject;

  @CommandLine.Option(names ={"-f1", "--file1"}, description = "The first file to scan.")
  public File file1 = null;

  @CommandLine.Option(names ={"-f2", "--file2"}, description = "The first file to scan.")
  public File file2 = null;

  @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help message.")
  public boolean helpRequested = false;
}
