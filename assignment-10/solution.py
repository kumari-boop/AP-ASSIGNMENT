class Address:
    def __init__(self, street, city, zip_code):
        self.street = street
        self.city = city
        self.zip_code = zip_code

    def display(self):
        return f"{self.street}, {self.city} - {self.zip_code}"


class Student:
    def __init__(self, name, age, address, courses=None):
        self.name = name
        self._age = None
        self.age = age
        self.address = address
        self.courses = courses if courses is not None else []

    @property
    def age(self):
        return self._age

    @age.setter
    def age(self, value):
        if value <= 0 or value > 120:
            raise ValueError("Age must be between 1 and 120")
        self._age = value

    def add_course(self, course):
        if course not in self.courses:
            self.courses.append(course)

    def display(self):
        print("Student Details:")
        print(f"Name: {self.name}")
        print(f"Age: {self.age}")
        print(f"Address: {self.address.display()}")
        print(f"Courses: {', '.join(self.courses) if self.courses else 'None'}")


class ScholarshipStudent(Student):
    def __init__(self, name, age, address, courses=None, scholarship_amount=0):
        super().__init__(name, age, address, courses)
        self.scholarship_amount = scholarship_amount

    def display(self):
        super().display()
        print(f"Scholarship Amount: {self.scholarship_amount}")


# Testing
addr = Address("MG Road", "Guwahati", "781001")

s1 = Student("Rupam", 20, addr)
s1.add_course("Math")
s1.add_course("Physics")

courses_ref = s1.courses
courses_ref.append("Chemistry")
s1.display()

print("\n--- Scholarship Student ---")
s2 = ScholarshipStudent("Gautam", 22, addr, ["CS", "AI"], 50000)
s2.add_course("ML")
s2.display()