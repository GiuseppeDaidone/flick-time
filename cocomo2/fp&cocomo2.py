def calculate_fp(fp_type, raw_count):
    fp_weights = {
        "simple": [3, 4, 3, 7],
        "average": [4, 5, 4, 10],
        "complex": [6, 7, 6, 15]
    }
    
    if fp_type not in fp_weights:
        raise ValueError("Invalid function point type. Choose from 'simple', 'average', or 'complex'")
    
    weights = fp_weights[fp_type]
    
    fp_count = sum(w * r for w, r in zip(weights, raw_count))
    
    return fp_count

def calculate_sloc(fp_count, sloc_per_fp):
    return fp_count * sloc_per_fp

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
    fp_type = input("Enter function point type (simple, average, complex): ")
    raw_count = [int(input("Enter number of %s function points: " % category)) for category in ["external inputs", "external outputs", "external inquiries", "internal logical files"]]
    size = calculate_fp(fp_type, raw_count)
    sloc_per_fp = float(input("Enter average lines of code per function point: "))
    mode = input("Enter mode (organic, semi-detached, embedded): ")
    sloc = calculate_sloc(size, sloc_per_fp)
    scale_factor = float(input("Enter scale factor: "))
    fp_scale_factor = (size/sloc) * scale_factor
    
    effort, time, staff = cocomo2(mode, size, fp_scale_factor)
    
    print("\nFunction Points: %d" % size)
    print("Estimated Source Lines of Code (SLOC): %d" % sloc)
    print("Function Points scale factor: %.3f" % fp_scale_factor)
    print("Effort: %.2f person-months" % effort)
    print("Development time: %.2f months" % time)
    print("Number of staff required: %.2f persons" % staff)
