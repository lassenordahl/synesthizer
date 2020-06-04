

def parse():
    final_sums = {}
    with open("tstjlogs.log") as logs:
        # rawbytes = logs.read()
        # enc = detect_encoding(rawbytes[0:2])
        for line in logs:
            case, time = line.split(":")[0:2]
            time = "".join(list(filter(lambda x: False if x is '\x00' or x is
                                       '\n' else True, list(time))))
            if not final_sums.get(case):
                final_sums[case] = {
                    "time_sum": int(time),
                    "count": 1
                }

            final_sums[case]["time_sum"] += int(time)
            final_sums[case]["count"] += 1

    for case, data in final_sums.items():
        avg_time = data["time_sum"] * 1.0 / data["count"]
        print("{}: {}ns".format(case, str(avg_time)))


if __name__ == "__main__":
    parse()
