package com.example.proyecto.request;

import java.io.File;

public class SendFileRequest {
  public File file;

  public SendFileRequest(File filePath) {
    this.file = filePath;
  }
}
