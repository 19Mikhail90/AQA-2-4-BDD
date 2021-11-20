package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV2;
import ru.netology.web.page.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTestPositive {

    @BeforeEach
    void shouldVerify() {
        val loginPage = open("http://localhost:9999", LoginPageV2.class);
        val authInfo = DataHelper.getAuthInfoValid();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    void shouldTransferMoneyFromSecondToFirst(int transfer) {

        val dahsboardPage = new DashboardPage();
        val startBalanceOfFirstCard = dahsboardPage.getBalanceOfFirstCard();
        val startBalanceOfSecondCard = dahsboardPage.getBalanceOfSecondCard();

        val transferPage = dahsboardPage.replenishBalanceFirsCard();
        val transferFrom2To1Card = DataHelper.getSecondCardInfo();
        transferPage.transferFrom(transferFrom2To1Card, transfer);
        val balanceFirstCard = DataHelper.getBalanceCardPlus(startBalanceOfFirstCard, transfer);
        val balanceSecondCard = DataHelper.getBalanceCardMinus(startBalanceOfSecondCard, transfer);

        assertEquals(balanceFirstCard, dahsboardPage.getBalanceOfFirstCard());
        assertEquals(balanceSecondCard, dahsboardPage.getBalanceOfSecondCard());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    void shouldTransferMoneyFromFirstToSecond(int transfer) {

        val dahsboardPage = new DashboardPage();
        val startBalanceOfFirstCard = dahsboardPage.getBalanceOfFirstCard();
        val startBalanceOfSecondCard = dahsboardPage.getBalanceOfSecondCard();

        val transferPage = dahsboardPage.replenishBalanceSecondCard();
        val transferFrom1To2Card = DataHelper.getFirstCardInfo();
        transferPage.transferFrom(transferFrom1To2Card, transfer);
        val balanceFirstCardAfterTrans = DataHelper.getBalanceCardMinus(startBalanceOfFirstCard, transfer);
        val balanceSecondCardAfterTrans = DataHelper.getBalanceCardPlus(startBalanceOfSecondCard, transfer);

        assertEquals(balanceFirstCardAfterTrans, dahsboardPage.getBalanceOfFirstCard());
        assertEquals(balanceSecondCardAfterTrans, dahsboardPage.getBalanceOfSecondCard());
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondMinus() {

        val dahsboardPage = new DashboardPage();
        val startBalanceOfFirstCard = dahsboardPage.getBalanceOfFirstCard();
        val startBalanceOfSecondCard = dahsboardPage.getBalanceOfSecondCard();

        val transferPage = dahsboardPage.replenishBalanceSecondCard();
        val transferFrom1To2Card = DataHelper.getFirstCardInfo();
        val transfer = -1000;
        transferPage.transferFrom(transferFrom1To2Card, transfer);
        val balanceFirstCardAfterTrans = startBalanceOfFirstCard + transfer;
        val balanceSecondCardAfterTrans = startBalanceOfSecondCard - transfer;

        assertEquals(balanceFirstCardAfterTrans, dahsboardPage.getBalanceOfFirstCard());
        assertEquals(balanceSecondCardAfterTrans, dahsboardPage.getBalanceOfSecondCard());
    }
}