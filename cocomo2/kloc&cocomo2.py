def cocomo2(mode, size, scale_factor):
    modes = {
        "organic": (2.4, 1.05, 2.5, 0.38),
        "semi-detached": (3.0, 1.12, 2.5, 0.35),
        "embedded": (3.6, 1.20, 2.5, 0.32)
    }
    
    if mode not in modes:
        raise ValueError("Invalid mode. Choose from 'organic', 'semi-detached', or 'embedded'")
    
    a, b, c, d = modes[mode]
    
    effort = a * (size ** b) * scale_factor
    time = c * (effort ** d)
    staff = effort / time
    
    return effort, time, staff


if __name__ == "__main__":
    size = float(input("Enter size of the software (KLOCs): "))
    mode = input("Enter mode (organic, semi-detached, embedded): ")
    scale_factor = float(input("Enter scale factor: "))
    
    effort, time, staff = cocomo2(mode, size, scale_factor)
    
    print("\nEffort: %.2f person-months" % effort)
    print("Development time: %.2f months" % time)
    print("Number of staff required: %.2f persons" % staff)
