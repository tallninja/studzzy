package application.controllers;

import application.controllers.utils.Password;
import application.models.*;

import java.sql.Date;
import java.util.List;

public class TestController {

    public static void main(String[] args) {

        Database.open();

          testUserController();
          testUnitController();
          testReminderController();
          testCatController();
          testExamController();
          testReportController();
          testAssignmentController();
          testPassword();

        Database.close();

    }

    public static void testUserController() {

        UserController.dropTable();

        User user1 = new User("Ernest", "Wambua", "ENE221-0106/2018",
                            "JKUAT", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                                "ernestwambua2@gmail.com", "password123");
        User user2 = new User("Junn", "Hope", "ENE221-0101/2018",
                "UON", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "junnhope2@gmail.com", "cake123");
        User user3 = new User("Gregory", "Mwangi", "ENE221-0097/2018",
                "KU", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "greg2@gmail.com", "cars123");
        User user4 = new User("Agnetta", "Khasamani", "ENE221-0110/2018",
                "JKUAT", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "agnetta2@gmail.com", "teacher123");
        User fakeUser = new User("Fake", "Fake", "Fake",
                "Fake", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "fake@email.com", "fake123");

        user1.save();
        user2.save();
        user3.save();
        user4.save();
        fakeUser.save();

        System.out.printf("%n List all users %n");

        List<User> users = UserController.getUsers();
        assert users != null;
        for(User user : users) {
            System.out.println(user);
        }

        System.out.printf("%n Update a User %n");
        System.out.println("Before Update:  " + UserController.getUser("fake@email.com"));

        fakeUser.update("Test", "Test", "Test",
                "Test", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        System.out.println("After Update:  " + UserController.getUser("fake@email.com"));

        System.out.printf("%n Delete a User %n");
        System.out.println("Before Delete:  " + UserController.getUser("fake@email.com"));

        fakeUser.delete();

        System.out.println("After Update:  " + UserController.getUser("fake@email.com"));

    }

    public static void testUnitController() {

        UnitController.dropTable();

        Unit unit1 = new Unit("PDE", "SMA2122", "Dr William Ndung'u", 20);
        Unit unit2 = new Unit("ODE", "SMA2121", "Mr Oketch", 77);
        Unit unit3 = new Unit("Programming 2", "ETI2301", "Mr Lincoln Kamau", 43);
        Unit unit4 = new Unit("Analogue 3", "ETI2303", "Mr Ngotho", 20);
        Unit unit5 = new Unit("IC", "ETI2344", "Mr Omae", 344);
        Unit unit6 = new Unit("Digital Electronics", "ETI3322", "Mr Kelvin Mwongera", 22);
        Unit fake = new Unit("fake", "fake", "fake", 12);

        unit1.save();
        unit2.save();
        unit3.save();
        unit4.save();
        unit5.save();
        unit6.save();
        fake.save();

        System.out.printf("%n List of all the units %n");
        List<Unit> units = UnitController.getUnits();
        assert units != null;
        for(Unit unit : units) {
            System.out.println(unit);
        }

        System.out.printf("%n Updating a unit %n");
        System.out.println("Before Update: " + UnitController.getUnit(fake.getUuid()));
        fake.update("test", "test", "test", 211);
        System.out.println("After Update: " + UnitController.getUnit(fake.getUuid()));

        System.out.printf("%n Deleting a unit %n");
        System.out.println("Before Deletion: " + UnitController.getUnit(fake.getUuid()));
        fake.delete();
        System.out.println("After Deletion: " + UnitController.getUnit(fake.getUuid()));

    }

    public static void testReminderController() {

        ReminderController.dropTable();

        Reminder reminder1 = new Reminder("reminder1", new Date(System.currentTimeMillis()));
        Reminder reminder2 = new Reminder("reminder1", new Date(System.currentTimeMillis()));
        Reminder reminder3 = new Reminder("reminder1", new Date(System.currentTimeMillis()));
        Reminder fake = new Reminder("fake", new Date(System.currentTimeMillis()));

        reminder1.save();
        reminder2.save();
        reminder3.save();
        fake.save();

        System.out.printf("%n List of all reminders %n");
        List <Reminder> reminders = ReminderController.getReminders();
        assert reminders != null;
        for(Reminder reminder : reminders) {
            System.out.println(reminder);
        }

        System.out.printf("%n Updating a reminder %n");
        System.out.println("Before Update: " + ReminderController.getReminder(fake.getUuid()));
        fake.update("test", new Date(System.currentTimeMillis()));
        System.out.println("After Update: " + ReminderController.getReminder(fake.getUuid()));

        System.out.printf("%n Deleting a reminder %n");
        System.out.println("Before Delete: " + ReminderController.getReminder(fake.getUuid()));
        fake.delete();
        System.out.println("After Delete: " + ReminderController.getReminder(fake.getUuid()));
    }

    public static void testCatController() {

        List<Unit> units = UnitController.getUnits();
        assert units != null;

        CatController.dropTable();
        Cat cat1 = new Cat(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);
        Cat cat2 = new Cat(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 2);
        Cat cat3 = new Cat(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);
        Cat cat4 = new Cat(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 2);
        Cat fake = new Cat(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);

        cat1.save();
        cat2.save();
        cat3.save();
        cat4.save();
        fake.save();

        System.out.printf("%n List of all CATs %n");
        List<Cat> cats = CatController.getCats();
        assert cats != null;
        for(Cat cat : cats) {
            System.out.println(cat);
        }

        System.out.printf("%n Edit a CAT %n");
        System.out.println("Before Update: " + CatController.getCat(fake.getUuid()));
        fake.update(new Date(System.currentTimeMillis()), 2);
        System.out.println("After Update: " + CatController.getCat(fake.getUuid()));

        System.out.printf("%n Delete a CAT %n");
        System.out.println("Before Delete: " + CatController.getCat(fake.getUuid()));
        fake.delete();
        System.out.println("After Update: " + CatController.getCat(fake.getUuid()));

    }

    public static void testExamController() {

        List<Unit> units = UnitController.getUnits();
        assert units != null;

        ExamController.dropTable();
        Exam exam1 = new Exam(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()));
        Exam exam2 = new Exam(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()));
        Exam exam3 = new Exam(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()));
        Exam exam4 = new Exam(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()));
        Exam fake = new Exam(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()));

        exam1.save();
        exam2.save();
        exam3.save();
        exam4.save();
        fake.save();

        System.out.printf("%n List of all exams %n");
        List<Exam> exams = ExamController.getExams();
        assert exams != null;
        for(Exam exam : exams) {
            System.out.println(exam);
        }

        System.out.printf("%n Edit an Exam %n");
        System.out.println("Before Update: " + ExamController.getExam(fake.getUuid()));
        fake.update(new Date(System.currentTimeMillis()));
        System.out.println("After Update: " + ExamController.getExam(fake.getUuid()));

        System.out.printf("%n Delete an exam %n");
        System.out.println("Before Delete: " + ExamController.getExam(fake.getUuid()));
        fake.delete();
        System.out.println("After Update: " + ExamController.getExam(fake.getUuid()));

    }

    public static void testReportController() {

        List<Unit> units = UnitController.getUnits();
        assert units != null;

        ReportController.dropTable();
        Report report1 = new Report(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);
        Report report2 = new Report(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 2);
        Report report3 = new Report(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);
        Report report4 = new Report(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 2);
        Report fake = new Report(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);

        report1.save();
        report2.save();
        report3.save();
        report4.save();
        fake.save();

        System.out.printf("%n List of all Reports %n");
        List<Report> reports = ReportController.getReports();
        assert reports != null;
        for(Report report : reports) {
            System.out.println(report);
        }

        System.out.printf("%n Edit a Report %n");
        System.out.println("Before Update: " + ReportController.getReport(fake.getUuid()));
        fake.update(new Date(System.currentTimeMillis()), 2);
        System.out.println("After Update: " + ReportController.getReport(fake.getUuid()));

        System.out.printf("%n Delete a Report %n");
        System.out.println("Before Delete: " + ReportController.getReport(fake.getUuid()));
        fake.delete();
        System.out.println("After Update: " + ReportController.getReport(fake.getUuid()));

    }

    public static void testAssignmentController() {

        List<Unit> units = UnitController.getUnits();
        assert units != null;

        AssignmentController.dropTable();
        Assignment assignment1 = new Assignment(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);
        Assignment assignment2 = new Assignment(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 2);
        Assignment assignment3 = new Assignment(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);
        Assignment assignment4 = new Assignment(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 2);
        Assignment fake = new Assignment(units.get((int) (Math.random() * units.size())).getUuid(), new Date(System.currentTimeMillis()), 1);

        assignment1.save();
        assignment2.save();
        assignment3.save();
        assignment4.save();
        fake.save();

        System.out.printf("%n List of all Assignments %n");
        List<Assignment> assignments = AssignmentController.getAssignments();
        assert assignments != null;
        for(Assignment assignment : assignments) {
            System.out.println(assignment);
        }

        System.out.printf("%n Edit a Assignment %n");
        System.out.println("Before Update: " + AssignmentController.getAssignment(fake.getUuid()));
        fake.update(new Date(System.currentTimeMillis()), 2);
        System.out.println("After Update: " + AssignmentController.getAssignment(fake.getUuid()));

        System.out.printf("%n Delete a Assignment %n");
        System.out.println("Before Delete: " + AssignmentController.getAssignment(fake.getUuid()));
        fake.delete();
        System.out.println("After Update: " + AssignmentController.getAssignment(fake.getUuid()));

    }

    public static void testPassword() {
        String password = "password";
        String hash = Password.hash(password);
        System.out.println(Password.verify(password, hash));
    }

}
