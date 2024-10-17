from flask import Flask, request, jsonify
import math

app = Flask(__name__)

@app.route('/calculate', methods=['POST'])
def calculate_projectile():
    data = request.get_json()
    velocity = data.get('velocity')
    angle = data.get('angle')
    print(data)
    
    if velocity is None or angle is None:
        return jsonify({'error': 'Missing velocity or angle'}), 400

    g = 9.81
    rad_angle = math.radians(angle)
    vx = velocity * math.cos(rad_angle)
    vy = velocity * math.sin(rad_angle)
    t_total = 2 * vy / g
    time_step = 0.1
    results = []

    t = 0.0
    while t <= t_total:
        x = vx * t
        y = vy * t - 0.5 * g * t * t
        if y >= 0:
            results.append((t, x, y))
        t += time_step

    # Adding the final step
    final_x = vx * t_total
    results.append((t_total, final_x, 0.0))

    return jsonify({'results': results})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
