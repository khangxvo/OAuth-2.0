
// --------------- Chart.js example chart -----------------
const ctx = document.getElementById('line_chart');

const labels = [
    'January',
    'Feburary',
    'March',
    'April',
    'May',
    'June',
];

const data = {
    labels: labels,
    datasets: [{
        label: 'My First Dataset',
        data: [65, 59, 80, 81, 20, 55, 40],
        fill: false,
        borderColor: 'rgb(75,192, 192)',
        tension: 0.1
    }, {
        label: 'My Second dataset',
        // backgroundCorlor: 'rgb(255, 255, 0)',
        borderColor: 'rgb(235, 185, 0)',
        data: [65, 2, 80, 81, 3, 55, 40]
    }]
};

new Chart(ctx, {
    type: 'line',
    data: data
})

// ------------------- My Chart -----------------------
const graph = document.getElementById('my_chart');
const days = ['Day 1', 'Day 2']
const info = {
    labels: days,
    datasets: [{
        label: "Your heart beat",
        data: [56, 78],
    }, {
        label: "Average heart beat",
        data: [90, 90]
    }]
};

new Chart(graph, {
    type: 'line',
    data: info
})
