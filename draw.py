import matplotlib.pyplot as plt
import numpy as np


input_sizes = np.array([300, 307, 314, 321, 328, 335, 342, 349, 356, 363, 370, 377, 384, 391, 398, 405, 412, 419, 426, 433, 440, 447, 454, 461, 468, 475, 482, 489, 496, 503, 510, 517, 524, 531, 538, 545, 552, 559, 566, 573, 580, 587, 594, 601, 608, 615, 622, 629, 636, 643, 650, 657, 664, 671, 678, 685, 692, 699, 706, 713, 720, 727, 734, 741, 748, 755, 762, 769, 776, 783, 790, 797, 804, 811, 818, 825, 832, 839, 846, 853, 860, 867, 874, 881, 888, 895, 902, 909, 916, 923, 930, 937, 944, 951, 958, 965, 972, 979, 986, 993, 1000])
actual_runtime = np.array([48, 25, 31, 25, 25, 26, 26, 27, 42, 29, 34, 29, 26, 26, 26, 28, 31, 27, 26, 28, 27, 28, 33, 33, 32, 35, 36, 42, 36, 38, 33, 35, 43, 35, 35, 36, 36, 38, 33, 36, 38, 43, 42, 44, 41, 44, 46, 45, 45, 48, 53, 51, 50, 51, 50, 46, 46, 46, 48, 56, 51, 54, 53, 62, 51, 52, 45, 48, 47, 56, 55, 49, 48, 50, 49, 51, 52, 53, 53, 52, 54, 53, 54, 54, 52, 53, 55, 54, 56, 57, 58, 61, 57, 59, 58, 59, 61, 58, 60, 60, 62])
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
