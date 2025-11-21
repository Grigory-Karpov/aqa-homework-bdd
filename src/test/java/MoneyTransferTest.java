import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // Получаем исходные балансы карт
        int firstCardIndex = 0; // Индекс первой карты в списке
        int secondCardIndex = 1; // Индекс второй карты в списке
        int initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardIndex);
        int initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardIndex);

        // Сумма перевода
        int amount = 1000;

        // Выполняем перевод со второй карты на первую
        var transferPage = dashboardPage.selectCardToTransfer(firstCardIndex);
        var cardInfo = DataHelper.getSecondCardInfo();
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), cardInfo);

        // Получаем итоговые балансы
        int finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardIndex);
        int finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardIndex);

        // Проверяем, что балансы изменились корректно
        assertEquals(initialBalanceFirstCard + amount, finalBalanceFirstCard);
        assertEquals(initialBalanceSecondCard - amount, finalBalanceSecondCard);
    }
}