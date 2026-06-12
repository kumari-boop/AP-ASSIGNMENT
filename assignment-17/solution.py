import re
import pytest


# Custom Exceptions
class InvalidEmailError(ValueError):
    def __init__(self, email):
        super().__init__(f"Invalid email address: '{email}'")


class UnderageError(Exception):
    def __init__(self, age):
        super().__init__(
            f"User must be at least 18 years old. Provided age: {age}"
        )


# Registration Service
class RegistrationService:
    EMAIL_PATTERN = r"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"

    def register_user(self, email: str, age: int) -> bool:
        # Internal invariant check
        assert isinstance(age, int), "Age must be an integer"

        # Email validation
        if email is None or email.strip() == "":
            raise InvalidEmailError(email)

        if not re.match(self.EMAIL_PATTERN, email):
            raise InvalidEmailError(email)

        # Age validation
        if age < 18:
            raise UnderageError(age)

        return True


# Pytest Fixture
@pytest.fixture
def service():
    return RegistrationService()


# Test Cases
def test_successful_registration(service):
    assert service.register_user(
        "john.doe@example.com",
        21
    ) is True


def test_invalid_email_format(service):
    with pytest.raises(InvalidEmailError):
        service.register_user(
            "invalid-email",
            25
        )


def test_empty_email(service):
    with pytest.raises(InvalidEmailError):
        service.register_user(
            "",
            25
        )


def test_null_email(service):
    with pytest.raises(InvalidEmailError):
        service.register_user(
            None,
            25
        )


def test_underage_user(service):
    with pytest.raises(UnderageError):
        service.register_user(
            "john@example.com",
            16
        )


def test_age_boundary_valid(service):
    assert service.register_user(
        "adult@example.com",
        18
    ) is True


def test_assertion_for_invalid_age_type(service):
    with pytest.raises(AssertionError):
        service.register_user(
            "john@example.com",
            "twenty"
        )