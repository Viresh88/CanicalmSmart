package com.example.assignmentshaaysoft

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentshaaysoft.bluetooth.BluetoothEventCallback
import com.example.assignmentshaaysoft.bluetooth.BluetoothManagerClass
import com.example.assignmentshaaysoft.databinding.FragmentBluetoothBinding
import com.numaxes.canicomgps.helper.PermissionHelper
import com.numaxes.canicomgps.injection.Injection
import com.numaxes.canicomgps.injection.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class BluetoothActivity : AppCompatActivity() , BluetoothEventCallback {

    private lateinit var binding: FragmentBluetoothBinding
    private lateinit var permissionHelper: PermissionHelper
    private var scanResultAdapter: BluetoothAdapterDevice? = null
    private var currentPassword: String? = null
    private var viewModel: DogViewModel? = null
    private var modelFactory: ViewModelFactory? = null
    private var scanResults = mutableListOf<Device>()
    private var selectedPosition by Delegates.notNull<Int>()
    private var bluetoothText: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var bluetoothAdapter: BluetoothAdapter
    var Dogname : String = ""
    private var isScanning = false
        set(value) {
            field = value
            runOnUiThread {
                binding.analyseBottom.text = if (value) getString(R.string.loading) else getString(R.string.analyze)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BluetoothManagerClass.setBluetoothManagerListener(this)

        permissionHelper = PermissionHelper(this)
        bluetoothAdapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        binding.analyseBottom.setOnClickListener { if (bluetoothAdapter.isEnabled) onScanButtonClick() else promptEnableBluetooth() }
        configureAdapterBle()
        setupRecyclerView()
        configureViewModel()
        updateCollarInData()

    }

    private fun configureViewModel() {
        modelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProvider(this, modelFactory!!).get(DogViewModel::class.java)
    }

    private fun updateCollarInData() {
        viewModel?.getAllDevices()?.observe(this) { data ->
            scanResults.clear()
            scanResults.addAll(data)
            scanResultAdapter?.notifyDataSetChanged()
        }
    }

    private fun configureAdapterBle() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, getString(R.string.blue_not_supported), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        } else {
            BluetoothManagerClass.associationSearchDeviceCanicalm()
        }
    }

    private fun setupRecyclerView() {
        scanResultAdapter = BluetoothAdapterDevice(scanResults, { position ->
            connectToDevice(position)
            selectedPosition = position
        }, { position ->
            // handle parameter click
        })

        binding.recyclerViewBluetooth.apply {
            adapter = scanResultAdapter
            layoutManager = LinearLayoutManager(this@BluetoothActivity, RecyclerView.VERTICAL, false)
            isNestedScrollingEnabled = false
        }
    }

    private fun connectToDevice(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val device = scanResults.getOrNull(position)
            device?.let {
                val newStatus = !it.status
                it.status = newStatus
                if (newStatus) {
                    device.address?.let { address ->
                        BluetoothManagerClass.connect(address)
                    }
                    delay(1000)
                    it.status = false
                } else {
                    device.address?.let {
                        BluetoothManagerClass.disconnect()
                    }
                }
                scanResultAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun onScanButtonClick() {
        isScanning = !isScanning
        if (isScanning) {
            startScan()
        } else {
            stopScan()
        }
    }

    private fun startScan() {
        BluetoothManagerClass.associationSearchDeviceCanicalm()
    }

    private fun stopScan() {
        //BluetoothManagerClass.stopScan()
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ENABLE_BLUETOOTH_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    startScan()
                }
            }
        }
    }

    companion object {
        const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    }

    @SuppressLint("MissingPermission")
    override fun onScanning(bluetoothDevice: BluetoothDevice) {
        this.runOnUiThread {
            val existingDeviceIndex = scanResults.indexOfFirst { it.address == bluetoothDevice.address }
            val newDevice = Device(bluetoothDevice.name, bluetoothDevice.address, false, getString(R.string.disconnect))
            if (existingDeviceIndex != -1) {
                scanResults[existingDeviceIndex] = newDevice
                scanResultAdapter?.notifyItemChanged(existingDeviceIndex)
            } else {
                scanResults.add(newDevice)
                scanResultAdapter?.notifyItemInserted(scanResults.size - 1)
            }

            scanResultAdapter?.notifyDataSetChanged()
        }
    }

    override fun onScanStarted() {
        TODO("Not yet implemented")
    }

    override fun onScanFinished() {
        TODO("Not yet implemented")
    }

    override fun onStartConnect(mac: String) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onConnectSuccess(bleDevice: BluetoothDevice?) {
      var  schedule = mapOf(
            "monday" to MutableList(24) { 0 },
            "tuesday" to MutableList(24) { 0 },
            "wednesday" to MutableList(24) { 0 },
            "thursday" to MutableList(24) { 0 },
            "friday" to MutableList(24) { 0 },
            "saturday" to MutableList(24) { 0 },
            "sunday" to MutableList(24) { 0 }
        )

        runOnUiThread {
            viewModel!!.saveDog(Dogname,20,bleDevice!!.address,60,schedule)
            Toast.makeText(this,"Connected Succssfully",Toast.LENGTH_SHORT).show()
        }
        navigateToHomePage(Dogname)



    }


    private fun navigateToHomePage(selectDog: String?) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("DOG_NAME", selectDog)
        }
        startActivity(intent)
        finish()
    }

    override fun onConnectDeviceSuccess(bleDevice: BluetoothDevice?) {
        TODO("Not yet implemented")
    }

    override fun onPasswordIncorrect() {
        TODO("Not yet implemented")
    }

    override fun onScanFailed(errorCode: Int) {
        TODO("Not yet implemented")
    }


    private fun associateDevice(name: String, device: BluetoothDevice) {
        BluetoothManagerClass.connect(device.address)
    }


    override fun showDeviceAssociationDialog(device: BluetoothDevice?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Associate Canicalm Device")
        builder.setMessage("Enter the name for the new device:")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            Dogname = input.text.toString()
            if (Dogname.isNotBlank()) {
                if (device != null) {
                    associateDevice(Dogname, device)
                }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
