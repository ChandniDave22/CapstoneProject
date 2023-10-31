import os
import pytest
from selenium import webdriver
from selenium.common import NoSuchElementException
from selenium.webdriver.common.by import By
from XLUtils import getData


CURRENT_DIR = os.getcwd()
TEST_DATA_FILE_PATH = CURRENT_DIR + r"\testdata\logincredential.xlsx"

@pytest.fixture(scope="class")
def chrome_driver_init(request):
    chrome_driver = webdriver.Chrome()
    request.cls.driver = chrome_driver
    yield
    chrome_driver.close()


@pytest.mark.usefixtures("chrome_driver_init")
class Test_URL_Chrome():

    @pytest.mark.parametrize("username, password", getData(TEST_DATA_FILE_PATH))
    def test_verify_login_sauce_demo(self, username, password):
        self.driver.get('https://www.saucedemo.com/')
        self.driver.implicitly_wait(5)
        self.driver.maximize_window()
        assert self.driver.title == "Swag Labs"

        username_input = self.driver.find_element(By.ID, "user-name")
        username_input.clear()
        username_input.send_keys(username)

        password_input = self.driver.find_element(By.ID, "password")
        password_input.clear()
        password_input.send_keys(password)

        self.driver.find_element(By.ID, "login-button").click()

        try:
            errorMsg = self.driver.find_element(By.XPATH, "//form//h3[@data-test ='error']").text
            assert errorMsg in ["Epic sadface: Username and password do not match any user in this service", "Epic sadface: Sorry, this user has been locked out."]
        except NoSuchElementException:
            assert self.driver.title == "Swag Labs"