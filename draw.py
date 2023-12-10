import matplotlib.pyplot as plt
import numpy as np


input_sizes = np.array([300, 307, 314, 321, 328, 335, 342, 349, 356, 363, 370, 377, 384, 391, 398, 405, 412, 419, 426, 433, 440, 447, 454, 461, 468, 475, 482, 489, 496, 503, 510, 517, 524, 531, 538, 545, 552, 559, 566, 573, 580, 587, 594, 601, 608, 615, 622, 629, 636, 643, 650, 657, 664, 671, 678, 685, 692, 699, 706, 713, 720, 727, 734, 741, 748, 755, 762, 769, 776, 783, 790, 797, 804, 811, 818, 825, 832, 839, 846, 853, 860, 867, 874, 881, 888, 895, 902, 909, 916, 923, 930, 937, 944, 951, 958, 965, 972, 979, 986, 993, 1000])
actual_runtime = np.array([50, 20, 31, 20, 20, 21, 20, 22, 19, 26, 18, 22, 20, 20, 25, 20, 20, 19, 20, 20, 21, 21, 20, 22, 24, 23, 25, 23, 24, 29, 27, 25, 30, 26, 31, 27, 30, 30, 29, 29, 29, 30, 28, 30, 35, 34, 32, 31, 30, 33, 33, 39, 33, 33, 34, 34, 39, 35, 36, 35, 34, 32, 32, 33, 44, 36, 33, 34, 36, 39, 36, 36, 34, 35, 37, 37, 38, 37, 39, 41, 37, 37, 39, 41, 40, 56, 42, 41, 39, 42, 44, 46, 45, 46, 42, 40, 42, 42, 43, 43, 43])
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
