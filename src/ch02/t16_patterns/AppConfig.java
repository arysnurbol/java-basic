package ch02.t16_patterns;

/**
 * Тапсырма 16 — Singleton шаблоны.
 * Кітап: "Шаблоны проектирования в Java" (110-б.)
 *
 * Мақсат: бүкіл қолданбада БІР ҒАНА конфигурация объектісі болуы.
 * Рецепт: private конструктор + private static өріс + public static getInstance().
 */
public class AppConfig {

    private static AppConfig instance;

    private String appName = "JavaPractice";

    private AppConfig() {
    }

    /** Жалғыз данасын қайтарады; әлі жасалмаса — жасайды. */
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
