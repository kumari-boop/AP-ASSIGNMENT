import pytest
import tempfile
import os


class ScoreProcessor:
    def process_score_file(self, file_path: str) -> int:
        try:
            with open(file_path, "r") as file:
                score = int(file.read().strip())

        except FileNotFoundError:
            print("Error: File not found.")
            raise

        except ValueError:
            print("Error: Invalid data format. File must contain an integer.")
            raise

        else:
            print("Data processed successfully")
            return score * 10

        finally:
            print("File cleanup completed")


# Test Cases

def test_successful_calculation():
    with tempfile.NamedTemporaryFile(mode="w", delete=False) as temp_file:
        temp_file.write("8")
        temp_path = temp_file.name

    processor = ScoreProcessor()

    try:
        result = processor.process_score_file(temp_path)
        assert result == 80
    finally:
        os.remove(temp_path)


def test_missing_file():
    processor = ScoreProcessor()

    with pytest.raises(FileNotFoundError):
        processor.process_score_file("nonexistent_file.txt")


def test_invalid_data_format():
    with tempfile.NamedTemporaryFile(mode="w", delete=False) as temp_file:
        temp_file.write("abc")
        temp_path = temp_file.name

    processor = ScoreProcessor()

    try:
        with pytest.raises(ValueError):
            processor.process_score_file(temp_path)
    finally:
        os.remove(temp_path)