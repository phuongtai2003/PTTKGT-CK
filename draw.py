import matplotlib.pyplot as plt
import numpy as np


input_sizes = np.array([300, 307, 314, 321, 328, 335, 342, 349, 356, 363, 370, 377, 384, 391, 398, 405, 412, 419, 426, 433, 440, 447, 454, 461, 468, 475, 482, 489, 496, 503, 510, 517, 524, 531, 538, 545, 552, 559, 566, 573, 580, 587, 594, 601, 608, 615, 622, 629, 636, 643, 650, 657, 664, 671, 678, 685, 692, 699, 706, 713, 720, 727, 734, 741, 748, 755, 762, 769, 776, 783, 790, 797, 804, 811, 818, 825, 832, 839, 846, 853, 860, 867, 874, 881, 888, 895, 902, 909, 916, 923, 930, 937, 944, 951, 958, 965, 972, 979, 986, 993, 1000])
actual_runtime = np.array([84, 53, 62, 60, 70, 51, 56, 64, 59, 56, 57, 60, 58, 63, 68, 72, 73, 76, 78, 74, 93, 96, 88, 77, 76, 71, 79, 74, 75, 75, 75, 76, 78, 79, 80, 81, 82, 82, 85, 85, 87, 88, 88, 90, 89, 91, 90, 107, 93, 97, 97, 97, 98, 99, 103, 103, 104, 107, 105, 105, 109, 109, 108, 110, 111, 112, 113, 112, 114, 119, 117, 118, 119, 124, 125, 122, 122, 123, 128, 125, 126, 129, 127, 130, 132, 134, 134, 133, 134, 139, 137, 141, 140, 139, 145, 141, 145, 146, 146, 147, 149])
# Theoretical complexities
complexity = input_sizes**3  # O(n^3)

# Theoretical complexity (using a secondary y-axis for a different scale)
title_algo = "Knapsack DWOA"
fig, ax1 = plt.subplots()
ax2 = ax1.twinx()
ax1.plot(input_sizes, complexity, label='Theoretical Complexity - O(n^3)', linestyle='--', color='blue')
ax2.plot(input_sizes, actual_runtime, label='Actual Runtime (' + title_algo + ')', marker='o', color='orange')

ax1.set_xlabel('Input size (' + str(input_sizes[0]) + ', ' + str(input_sizes[-1] + 1) + ')' + ', ' + 'step = ' + str(input_sizes[1] - input_sizes[0]) + ', ' + 'total = ' + str(len(input_sizes)))
ax1.set_ylabel('Theoretical Complexity (log scale)', color='blue')
ax2.set_ylabel('Actual Runtime (milliseconds)', color='orange')

plt.title('Theoretical Complexity vs Actual Runtime')
fig.legend(loc="upper left", bbox_to_anchor=(0,1), bbox_transform=ax1.transAxes)
plt.grid(True)
plt.show()
