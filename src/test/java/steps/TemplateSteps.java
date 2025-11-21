package steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import page.DashboardPage;
import page.LoginPage;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateSteps {
    private DashboardPage dashboardPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = new DataHelper.AuthInfo(login, password);
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void transferMoney(int amount, String fromCardNumber, int toCardIndex) {
        var transferPage = dashboardPage.selectCardToTransfer(toCardIndex - 1);
        var cardInfo = new DataHelper.CardInfo(fromCardNumber);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), cardInfo);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void verifyFinalBalance(int cardIndex, int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalance(cardIndex - 1);
        assertEquals(expectedBalance, actualBalance);
    }
}