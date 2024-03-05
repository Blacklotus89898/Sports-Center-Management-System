import endpoints.schedule as schedule

if __name__ == "__main__":
    print(schedule.create_schedule(2021))
    print(schedule.get_schedules())
    print(schedule.get_schedule(2021))
