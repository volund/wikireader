package com.putskul_productions.wikireader;

public class App {
    final static App shared = new App();

    final Settings settings = new Settings();
    final StorageBackend storage = new StorageBackend();
    final Model model = new Model();
}
