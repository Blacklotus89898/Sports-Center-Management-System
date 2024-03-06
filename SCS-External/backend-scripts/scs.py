import endpoints.schedule as schedule
import endpoints.custom_hours as custom_hours

if __name__ == "__main__":
    print("Schedule")
    print("Create Schedule `2021`")
    print(schedule.create_schedule(2021))

    print("Get Schedules && Get Schedule `2021`")
    print(schedule.get_schedules())
    print(schedule.get_schedule(2021))

    print("\n====================\n")

    print("Custom Hours")
    print("Create Custom Hours `test`")
    print(custom_hours.create_custom_hours("test", "test", "2021-01-01", "00:00", "23:59", 2021))
    print(custom_hours.get_custom_hours())

    print("Get Custom Hours by Name `test`")
    print(custom_hours.get_custom_hours_by_name("test"))
    
    print("Get Custom Hours by Date `2021-01-01`")
    print(custom_hours.get_custom_hours_by_date("2021-01-01"))

    print("Update Custom Hours `test`")
    print(custom_hours.update_custom_hours("test", "new description", "2021-01-01", "00:00", "23:59", 2021))
    print(custom_hours.get_custom_hours_by_name("test"))

    print("Create Custom Hours `test1` and `test2` and Delete Custom Hours `test` and Get Custom Hours")
    print(custom_hours.create_custom_hours("test1", "test", "2021-01-01", "00:00", "23:59", 2021))
    print(custom_hours.create_custom_hours("test2", "test", "2021-01-01", "00:00", "23:59", 2021))
    print(custom_hours.delete_custom_hours("test"))
    print(custom_hours.get_custom_hours())

    print("Delete All Custom Hours and Get Custom Hours")
    print(custom_hours.delete_all_custom_hours())
    print(custom_hours.get_custom_hours())
